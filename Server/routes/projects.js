const router = require('express').Router();
const {admin, db} = require('../utils/admin');

//create a new project
router.route('/project').post((req, res) => {
    const creationDate = Date.now();
    db.collection("projects").add(req.body,{creationDate: creationDate})
    .then(project=>{
        return res.status(201).json({message:"create project success",id:project.id})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

//delete a project
router.route('/project/:id').delete((req,res) => {
    db.collection("projects").doc(req.params.id).delete()
    .then(project => {
        return res.status(200).json({message: "Project successfully deleted", name:project.name})
    })
    .catch(err => res.status(400).json('Error: ' + err));
});

//update a project
router.route('/project/:id').put((req, res) => {
    let FieldValue = admin.FieldValue
    let docRef = db.collection('projects').doc(req.params.id);
    docRef.update(req.body)
    .then(() => {
        let updateTimestamp = docRef.update({modificationDate: FieldValue.serverTimestamp()});
        return res.status(200).json({message:"update project success"},updateTimestamp)
    })
    .catch(err => res.status(400).json('Error: ' + err));
});

//get a project info
router.route('/project/:id').get((req, res) => {
    db.collection('projects').doc(req.params.id)
    .then(project => {
        return res.status(200).json({message:"get project success"},project)
    })
    .catch(err => res.status(400).json('Error: ' + err));
});

//query and get a list of projects 
router.route('/projects/:userID').get((req, res) => {

    let res1 = db.collection('projects').doc(req.params.id).where("collaborators", 'array-contains', req.params.userID)
    .then(resp => {
        let projects1 = []
        if(resp.empty){
            return projects1
        }
        else{
            resp.forEach(projects => {
                projects1.push(projects.data())
            });
            return projects1
        }   
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
    let res2 = db.collection('projects').doc(req.params.id).where("administrator", 'array-contains', req.params.userID)
    .then(resp => {
        let projects2 = []
        if(resp.empty){
            return projects2
        }
        else{
            resp.forEach(projects => {
                projects2.push(projects.data())
            });
            return projects2
        }   
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })


    return res1.push(...res2)

});



module.exports = router;
