const { newEnforcer } = require('casbin');

const enforcer_root = newEnforcer('casbin/model.conf', 'casbin/policy.csv')

async function enforce(s, o, a) {
  const enforcer = await enforcer_root
  console.log('s: ' + s)
  console.log('o: ' + o)
  console.log('a: ' + a)
  const roles = await enforcer.getRolesForUser(s)
  if (roles.length == 0){ 
    await enforcer.addRoleForUser(s, "free")
    console.log("added free") 
  }
  console.log(roles)

  r = await enforcer.enforce(s, o, a);
  return {res: r, sub: s, obj: o, act: a};
}

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
