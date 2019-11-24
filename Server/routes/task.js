const router = require('express').Router();
const {admin, db} = require('../utils/admin');

router.route('/task').post((req, res) => {
    const creationDate = Date.now();
    const status = "pending";

    db.collection("tasks").add(req.body,{creationDate: creationDate, status:status})
    .then(task => {
        return res.status(201).json({message:"create task success",id:task.id})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

//query a list of tasks of a project
router.route('/tasks/:projectID').get((req,res) => {
    db.collection("tasks").where('projectID','==',req.params.projectID).get()
    .then(resp => {
        if(resp.empty){
            return res.json({message:"No matching tasks."})
        }
        else{
            let tasks = []
            resp.forEach(task => {
                tasks.push(task.data())
            });
            return res.json(tasks)
        }   
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

//upsate a task
router.route('/task/:id').put((req,res) => {
    db.collection("tasks").doc(req.params.id).update(req.body)
    .then(() => {
        return res.status(200).json({message:"update task success"})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

module.exports = router;