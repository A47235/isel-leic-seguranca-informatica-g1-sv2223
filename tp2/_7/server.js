const express = require('express')
const cookieParser = require('cookie-parser');
const axios = require('axios');
const env = require('dotenv')
const FormData = require('form-data');
const jwt = require('jsonwebtoken');
const crypto = require('crypto')
const {execute, enforce} = require('./casbin/casbinHelper')


const port = 3001

const toReadWrite = {
    'GET': 'read',
    'POST': 'write'
}

env.config()

const CLIENT_ID = process.env.CLIENT_ID
const CLIENT_SECRET = process.env.CLIENT_SECRET
const CALLBACK = 'callback'

const app = express()
app.use(cookieParser());
app.use(express.urlencoded({ extended: true }))


app.get('/', (req, resp) => {
    resp.send('<a href=/login>Use Google Account</a><br></br><a href=/list>Check Task Lists</a><br></br><a href=/admin>Admin Room</a>')
})



let sessions = [];

function login(req, resp) {
    let state = crypto.randomUUID()
    
    sessions.push({'state':state})
    resp.redirect(302,
        'https://accounts.google.com/o/oauth2/v2/auth?'
        
        + 'client_id='+ CLIENT_ID +'&'
        
        + 'scope=openid%20email%20https://www.googleapis.com/auth/tasks&'

        + 'state='+ state +'&'
        
        + 'response_type=code&'
        
        + 'redirect_uri=http://localhost:3001/'+CALLBACK)
}


function callback(req, resp) {

    let session = sessions.find((element) => req.query.state == element.state)

    if (session) {

            console.log('making request to token endpoint')
            const form = new FormData();
            form.append('code', req.query.code);
            form.append('client_id', CLIENT_ID);
            form.append('client_secret', CLIENT_SECRET);
            form.append('redirect_uri', 'http://localhost:3001/'+CALLBACK);
            form.append('grant_type', 'authorization_code');
        
            axios.post(
                'https://www.googleapis.com/oauth2/v3/token', 
                form,
                { headers: form.getHeaders() }
              )
              .then(function (response) {

                console.log(response.data)

                var jwt_payload = jwt.decode(response.data.id_token)
                console.log(jwt_payload)
        
                resp.cookie("AuthCookie", session.state)
                sessions.map(
                    x => {
                        if (x.state == session.state) {
                            x.token = response.data.access_token
                            x.email = jwt_payload.email
                        }
                        return x
                    }
                )
                resp.send(
                    '<div> callback with code = <code>' + req.query.code + '</code></div><br>' +
                    '<div> client app received access code = <code>' + response.data.access_token + '</code></div><br>' +
                    '<div> id_token = <code>' + response.data.id_token + '</code></div><br>' +
                    '<div> Hi <b>' + jwt_payload.email + '</b> </div><br>' +
                    'Go back to <a href="/">Home screen</a>'
                );
              })
              .catch(function (error) {
                console.log(error)
                resp.send()
              });

    }
}


function authMiddleware(req, resp, next) {
    if (!req.cookies.AuthCookie || !getToken(req.cookies.AuthCookie)) {
        resp.redirect('/login')
    } else {
        next()
    }
}

function rbacMiddleware(req, resp, next) {
    const sub = getEmail(req.cookies.AuthCookie)
    const act = toReadWrite[req.method];
    enforce(sub, req.path, act).then(decision => {
        execute(decision)
        if(decision.res) next()
        else resp.redirect('/unauthorized')
    })
}

function getToken(state) {

    let session = sessions.find((element) => state == element.state)
    if (session) {
        return session.token
    } else return null

}

function getEmail(state) {

    let session = sessions.find((element) => state == element.state)
    if (session) {
        return session.email
    } else return null

}


function getTaskList(req, resp) {
    const token = getToken(req.cookies.AuthCookie)
    axios.get(
        `https://tasks.googleapis.com/tasks/v1/lists/${req.params.id}/tasks`, 
        { headers: {Authorization: `Bearer ${token}` } }
    ).then(function (response) {

        console.log(response.data)
        let listHtml = response.data.items.map(item => `<div><h1>Name: ${item.title}</h1><p>Notes: ${item.notes ? item.notes : "There's no notes for this task"}<br></br>LastUpdated: ${item.updated}</p></div>`).join("<br></br>")
        listHtml += "<br></br><h1>Create a new task</h1>" +
        "<form method='post' action='/list/"+req.params.id+"'>" + 
        "<label for='title'>Title:</label><br></br>" +
        "<input type='text' id='title' name='title'><br></br>" +
        "<label for='notes'>Notes:</label><br></br>" +
        "<input type='text' id='notes' name='notes'><br></br><br></br>" +
        "<input type='submit' value='Submit'>" +
        "</form>"
        
        resp.send(listHtml)
      })
      .catch(function (error) {
        console.log(error)
        resp.send()
      });
}

function getAllTaskLists(req, resp) {
    const token = getToken(req.cookies.AuthCookie)
    axios.get(
        `https://tasks.googleapis.com/tasks/v1/users/@me/lists`, 
        { headers: {Authorization: `Bearer ${token}` } }
    ).then(function (response) {
        console.log(response.data.items)

        let html = '<h1>All TaskLists:</h1><br></br>'

        response.data.items.forEach(element => {
            html = html + `<a href='/list/${element.id}'>Title: ${element.title}</a><br></br>`
        });

        resp.send(html)
      })
      .catch(function (error) {
        console.log(error)
        resp.send()
      });
}

function postTask(req, resp) {

    const token = getToken(req.cookies.AuthCookie)
    console.log(req.body)
    axios.post(
        `https://tasks.googleapis.com/tasks/v1/lists/${req.params.id}/tasks`, 
        { title: req.body.title, notes: req.body.notes },
        { headers:{
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        } }
    ).then(function (response) {
        resp.redirect(`/list/${req.params.id}`)
      })
      .catch(function (error) {
        console.log(error)
        resp.send()
      });

}

function unauthorized(req, resp){
    resp.send('<p>YOU ARE UNHAUTORIZED</p>')
}

function admin_room(req, resp){
    resp.send('<p>Welcome to the Admin Room</p> <p>Only Admin users can access it.</p>')
}

app.get('/login', login)

app.get('/' + CALLBACK, callback)

app.get('/list/:id', authMiddleware, rbacMiddleware, getTaskList)
app.get('/list', authMiddleware, rbacMiddleware, getAllTaskLists)

app.post('/list/:id', authMiddleware, rbacMiddleware, postTask)

app.get('/unauthorized', unauthorized)

app.get('/admin', authMiddleware, rbacMiddleware, admin_room)



app.listen(port, (err) => {
    if (err) {
        return console.log('something bad happened', err)
    }
    console.log(`server is listening on ${port}`)
})