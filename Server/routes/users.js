const router = require('express').Router();
const { admin , db } = require('../utils/admin');

// create a user and upload it's profile to firestore
router.route('/user').post((req, res) => {
    const name = req.body.name;
    const email = req.body.email;
    const password = req.body.password;

    const newUser = {
        name,
        password,
        email
    };

    admin.auth().createUser(newUser)
        .then(function(userRecord) {
          // See the UserRecord reference doc for the contents of userRecord.
          let user = db.collection('users').doc(userRecord.uid);
          return (
            user.set({
                name: newUser.name
            })
          )
        })
        .then(()=>{
            res.status(201).json({message:'Successfully created new user'});
        })
        .catch(function(error) {
          console.log('Error creating new user:', error);
        });
});

//update user password
router.route("/userupdate/:id").put((req ,res) => {
    admin.auth().updateUser(req.params.id,{
        password:req.body.password
    })
    .then(()=>{
        return res.status(200).json({message:"update password success"})
    })
    .catch(err=>{
        return res.status(400).json({message:"update password failed", err:err})
    })
});

router.route("/user/:id").get((req,res) => {
    admin.auth().getUser(req.params.id)
    .then(userRecord=>{
        return res.status(200).json({message:"get user info success"},userRecord)
    })
    .catch(err => {
        return res.status(400).json('Error: ' + err)
    })
});


//query user by name
router.route("/users").get((req,res) => {

    db.collection('users').where("name",">=", req.body.name)
    .then(resp => {
        if(resp.empty){
            return res.json({message:"No matching users."})
        }
        else{
            let users =[]
            resp.forEach(user => {
                users.push(user.data())
            });
            return res.json(users)
        }   
    })
    .catch(err => {
        return res.status(400).json('Error: ' + err)
    })
});


module.exports = router;
