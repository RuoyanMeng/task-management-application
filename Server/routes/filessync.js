const router = require('express').Router();
const {admin, db} = require('../utils/admin');


//create a new project
router.route('/files/imagesU').put((req, res) => {
    db.collection("files").doc(req.body.projectID).update({
        images: admin.firestore.FieldValue.arrayUnion(req.body.imageUrl)
    })
    .then(() => {
        return res.status(201).json({message:"update image url success"})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

router.route('/files/imagesD').put((req, res) => {
    db.collection("files").doc(req.body.projectID).update({
        images: admin.firestore.FieldValue.arrayRemove(req.body.imageUrl)
    })
    .then(() => {
        return res.status(201).json({message:"Remove image url success"})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

router.route('/files/docsU').put((req, res) => {
    db.collection("files").doc(req.body.projectID).update({
        images: admin.firestore.FieldValue.arrayUnion(req.body.imageUrl)
    })
    .then(() => {
        return res.status(201).json({message:"update file url success"})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

router.route('/files/docsD').put((req, res) => {
    db.collection("files").doc(req.body.projectID).update({
        images: admin.firestore.FieldValue.arrayRemove(req.body.imageUrl)
    })
    .then(() => {
        return res.status(201).json({message:"Remove file url success"})
    })
    .catch(err =>{
        return res.status(400).json("Error"+err)
    })
});

