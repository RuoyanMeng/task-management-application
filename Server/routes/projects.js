const router = require('express').Router();
const { db} = require('../utils/admin');

//create a new project
router.route('/project').post((req, res) => {
    const creationDate = new Date();
    // console.log(creationDate.toISOString())
    let project = {
        name : req.body.name,
        description: req.body.description,
        isPersonal: req.body.isPersonal,
        administrator : req.body.administrator,
        creationDate : creationDate.toISOString(),
        collaborators: req.body.collaborators,
        isFavorite:req.body.isFavorite
    }
    db.collection("projects").add(project)
    .then(projectRecord=>{
        let project = db.collection("projects").doc(projectRecord.id)
        return (
            project.update({
            id:projectRecord.id
            })
        )  
    })
    .then(() =>{
        return res.status(201).json({message:"create project success"})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

//get a project info
router.route('/project/:id').get((req, res) => {
    db.collection('projects').doc(req.params.id).get()
    .then(project => {
        return res.status(200).json({message:"get project success", project: project.data()})
    })
    .catch(err => res.status(400).json('Error: ' + err));
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
    let docRef = db.collection('projects').doc(req.params.id);
    docRef.update(req.body)
    .then(() => {
        let modificationDate = new Date();
        docRef.update({modificationDate:modificationDate.toISOString()})
        .then(project => {
            return  res.status(201).json({message:"update project success"})
        })
        .catch(err => res.status(400).json('Error: ' + err));
    })
    .catch(err => res.status(400).json('Error: ' + err));
});



//query and get a list of projects 
router.route('/projects/:userID').get((req, res) => {
    let projects = []
    let projects2 = []

    //start with collaborate projects of current user
    let res1 = db.collection('projects').where("collaborators", 'array-contains', req.params.userID).get()
    .then(resp => {
        if(resp.empty){
            console.log("empty here")
        }
        else{
            //let userid = req.params.userID
            resp.forEach(project => {
                //get if favorite project
                //console.log(project.data().isFavorite[userid])
                projects.push(project.data())
            });        
        }

        //here to collect admin projects of current user
        db.collection('projects').where("administrator.user", "==", req.params.userID).get()
        .then(resp => {
            if(resp.empty){
                return projects2
            }
            else{
                resp.forEach(projects => {
                    projects2.push(projects.data())
                });
                projects.push(...projects2)
                return res.json(projects)
            }   
            })
        .catch(err =>{
            return res.status(400).json("Error"+err)
        })
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })

    

});



module.exports = router;
