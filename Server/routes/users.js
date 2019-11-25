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
          console.log(userRecord.uid)
            
          let user = db.collection('users').doc(userRecord.uid);
          return (
            user.set({
                uid: userRecord.uid,
                name: newUser.name,
                email: newUser.email
            })
          )
        })
        .then(()=>{
            res.status(201).json({message:'Successfully created new user'});
        })
        .catch(function(error) {
            res.status(201).json({message:'Error creating new user', err:error});
        });
});

//update user password
router.route("/user/updpsw/:id").put((req ,res) => {
    admin.auth().updateUser(req.params.id,{
        password:req.body.password
    })
    .then(()=>{
        return res.status(201).json({message:"update password success"})
    })
    .catch(err=>{
        return res.status(400).json({message:"update password failed", err:err})
    })
});

//get a user info
router.route("/user/:id").get((req,res) => {
    db.collection('users').doc(req.params.id).get()
    .then(userRecord=>{
        return res.status(200).json({message:"get user info success",user: userRecord.data()})
    })
    .catch(err => {
        return res.status(400).json('Error: ' + err)
    })
});


//query user by name
router.route("/users/:name").get((req,res) => {

    db.collection('users').where("name",">=", req.params.name).get()
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
