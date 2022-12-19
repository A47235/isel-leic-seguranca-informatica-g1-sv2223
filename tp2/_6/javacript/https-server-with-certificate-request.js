// Código retirado do github da disciplina
// https://github.com/isel-deetc-computersecurity/seginf-inv2223-public/blob/main/HTTPS-server/https-server-base.js

// Built-in HTTPS support
const https = require("https");
const http = require("http");
// Handling GET request (npm install express)
const express = require("express");
// Load of files from the local file system
var fs = require('fs'); 

const PORT = 4433;
const app = express();

// Get request for resource /
app.get("/", function (req, res) {
    console.log(
        req.socket.remoteAddress
        + ' ' + req.socket.getPeerCertificate().subject?.CN
        + ' ' + req.method
        + ' ' + req.url);
    res.send("<html><body>Secure Hello World with node.js with the certificate " + req.socket.getPeerCertificate().subject?.CN + "</body></html>");
});


// configure TLS handshake
const options = {
    key: fs.readFileSync('./secure-server-pfx.pem'),
    cert: fs.readFileSync('./secure-server-certificate.pem'),
    ca: [fs.readFileSync('./CA2.pem')], // necessário para que o certificado Alice_2 possa ser utilizado
    requestCert: true, 
    //rejectUnauthorized: true
};


// Para poder utilizar o certificado Alice_2 foi necessário instalar os certificados Alice_2, CA2-int e CA2
// instalar o pfx Alice_2 no browser 

// Create HTTPS server
https.createServer(options, app).listen(PORT, 
    function (req, res) {
        console.log("Server started at port " + PORT);
    }
);
