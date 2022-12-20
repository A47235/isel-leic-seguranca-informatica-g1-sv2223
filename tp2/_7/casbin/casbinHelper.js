const { newEnforcer } = require('casbin');

async function enforce(s, o, a) {
    const enforcer = await newEnforcer('casbin/model.conf', 'casbin/policy.csv');
    console.log('s: ' + s)
    console.log('o: ' + o)
    console.log('a: ' + a)
    const roles = await enforcer.getRolesForUser(s)
    if ((roles).length == 0){ 
      await enforcer.addRoleForUser(s, "free")
      console.log("added free") 
    }
    console.log(roles)

    r = await enforcer.enforce(s, o, a);
    return {res: r, sub: s, obj: o, act: a};
}

/*
async function addRolesToUser(sub, roles) {
    const e = await enforcerPromise;
    await Promise.all(roles.map(role => e.addRoleForUser(sub, role)));
}
*/

async function execute(decision) {
    console.log(decision);
    if (decision.res == true) {
      console.log("permit operation")
    } else {
      console.log("deny operation")
    }  
}

module.exports.execute = execute;
module.exports.enforce = enforce;
//module.exports.addRolesToUser = addRolesToUser;