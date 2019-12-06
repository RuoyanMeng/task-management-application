const router = require('express').Router();
const { db, admin } = require('../utils/admin');

let randomNumber = (min, max) => Math.floor(Math.random() * (max - min)) + min;
let randomBoolean = () =>  Math.random() >= 0.5;
let randomDate = (from, to) => {
    from = from.getTime();
    to = to.getTime();
    return new Date(from + Math.random() * (to - from));
};

let sortByDateProperty = (project1, project2, property, isAscending) => {

    let project1Before = isAscending ? -1 : 1;
    let project2Before = isAscending ? 1 : -1;

    if(!project1.hasOwnProperty(property))
        return !project2.hasOwnProperty(property) ? 0 : -1;
    else if(!project2.hasOwnProperty(property))
        return 1;
    else{
        let date1 = new Date(project1[property]);
        let date2 = new Date(project2[property]);
        return (date1 === date2) ? 0 : ((date1 > date2) ? project1Before : project2Before)
    }
    //TODO fix sorting
};


router.route("/fix").get((req, res) => {

    db.collection('projects').get().then(resp => {
        let batch = db.batch();

        resp.docs.forEach((queryDocumentSnapshot => {
            let project = queryDocumentSnapshot.data();
            if(randomBoolean()){
                project.modificationDate = new Date().toISOString();
                batch.update(queryDocumentSnapshot.ref, project);
            }
        }));

        batch.commit().then( documentReference => {
            return res.status(201).json({message: "update projects success"})
        })
        .catch(err => {
            return res.status(400).json({err: err, message: err.message})
        });

    })

});

router.route("/generateData").get((req, res) => {

    db.collection('users').get().then(resp => {

        let generatedProjects = [];

        let users = resp.docs.map((queryDocumentSnapshot => queryDocumentSnapshot.data()));
        let userNb = users.length;

        for(let  i = 0; i < userNb; ++i){
            let admin = users[i];
            let project = {
                name: admin.name + "Project" + "Personal" + i + "Name",
                description: admin.name + "Project" + "Personal" + i + "Description",
                isPersonal: true,
                administrator: admin.uid,
                creationDate: new Date().toISOString(),
                deadline: randomDate(new Date("2019-12-15"), new Date("2020-03-25")).toISOString()
            };

            let collaborators = [];
            let favorites = {};
            favorites[admin.uid] = randomBoolean();

            for(let j = 0; j < 3; ++j){
                let indexCollab;

                do
                    indexCollab = randomNumber(0, userNb);
                while(indexCollab === i);

                let collabId = users[indexCollab].uid;

                let favoriteValue = randomBoolean();
                collaborators.push(collabId);

                favorites[collabId] = favoriteValue;
            }

            let project2 = {
                name: admin.name + "Project" + "Personal" + (i + 10) + "Name",
                description: admin.name + "Project" + "Personal" + (i + 10) + "Description",
                isPersonal: false,
                administrator: admin.uid,
                creationDate: new Date().toISOString(),
                collaborators: collaborators,
                isFavorite: favorites,
                deadline: randomDate(new Date("2019-12-15"), new Date("2020-03-25")).toISOString()
            };

            generatedProjects.push(project);
            generatedProjects.push(project2);

        }

        let batch = db.batch();

        generatedProjects.forEach((project) => {
            let ref = db.collection("projects").doc();
            batch.set(ref, project);
        });

        batch.commit().then( documentReference => {
            return res.status(201).json({message: "create projects success"})
        })
        .catch(err => {
            return res.status(400).json({err: err, message: err.message})
        });

    })
    .catch(err => {
        return res.status(400).json({err: err.message})
    })
});

router.route("/generateProjU").get((req, res) => {

    db.collection('projects').get().then(resp => {
        let projectsU = [];
        let projects = resp.docs.map(queryDocumentSnapshot => {
            return ({
                    id: queryDocumentSnapshot.id,
                    modificationDate: new Date().toISOString()
                })
        })

        projects.forEach(item =>{
            projectsU.push(item);
        })

        let batch = db.batch();

        projectsU.forEach((projectU) => {
            let ref = db.collection("projects").doc(projectU.id);
            batch.update(ref, projectU)
        });

        batch.commit().then(documentReference => {
            return res.status(201).json({ message: "update project success" })
        })
            .catch(err => {
                return res.status(400).json({ err: err, message: err.message })
            });

    })
        .catch(err => {
            return res.status(400).json({ err: err.message })
        })
})

//create a new project
router.route('/project').post((req, res) => {

    let uid = req.uid;
    const creationDate = new Date();
    // console.log(creationDate.toISOString())
    let project = {
        name: req.body.name,
        description: req.body.description,
        isPersonal: req.body.isPersonal,
        administrator: uid,
        creationDate: creationDate.toISOString(),
        collaborators: req.body.collaborators,
        isFavorite: req.body.isFavorite,
        modificationDate:creationDate.toISOString()
    };

    if(!project.isPersonal){
        project.collaborators = req.body.collaborators;
        project.isFavorite = req.body.isFavorite;
    }

    db.collection("projects").add(project)
        .then(projectRecord => {
            let project = db.collection("projects").doc(projectRecord.id);
            db.collection("files").doc(projectRecord.id).set({
                images: null,
                docs: null
            });

            return (
                project.update({
                    id: projectRecord.id
                })
            )
        })
        .then(() => {
            return res.status(201).json({ message: "create project success" })
        })
        .catch(err => {
            return res.status(400).json({ err: err, message: "Error creating the project" })
        })
});

//get a project info
router.route('/project/:id').get((req, res) => {
    db.collection('projects').doc(req.params.id).get()
        .then(project => {
            const projectData = project.data();

            if (!projectData)
                return res.status(404).json({ message: "No project with this id" });
            else
                return res.status(200).json({ message: "get project success", data: projectData })

        })
        .catch(err => res.status(400).json({ err: err, message: "Error getting the project" }));
});

//delete a project
router.route('/project/:id').delete((req, res) => {
    db.collection("projects").doc(req.params.id).delete()
        .then(project => {
            return res.status(200).json({ message: "Project successfully deleted", data: project.name })
        })
        .catch(err => res.status(400).json({ err: err, message: "Error deleting the project" }));
});

//update a project
router.route('/project/:id').put((req, res) => {
    let docRef = db.collection('projects').doc(req.params.id);
    docRef.update(req.body)
        .then(() => {
            let modificationDate = new Date();
            docRef.update({ modificationDate: modificationDate.toISOString() })
                .then(project => {
                    return res.status(201).json({ message: "update project success" })
                })
                .catch(err => res.status(400).json({ err: err, message: "Error updating the project modification date" }));
        })
        .catch(err => res.status(400).json({ err: err, message: "Error updating the project" }));
});


//update isFavorite
router.route('/project/isFavoriteU/:id').put((req, res) => {

    let uid = req.body.uid
    let modificationDate = new Date();

    let docRef = db.collection('projects').doc(req.params.id);
    docRef.get()
        .then(projectRef => {
            project = projectRef.data().isFavorite
            for (let key in project) {
                if (key === uid) {
                    project[key] = req.body.isFavorite
                }
            }
            return (
                docRef.update({
                    isFavorite: project,
                    modificationDate: modificationDate.toISOString()
                })
            )
        })
        .then(() => {
            return res.status(201).json({ message: "update isFavorite success" })
        })
        .catch(err => {
            return res.status(400).json("Error" + err)
        })
})

//query and get a list of projects 
router.route('/projects/:kind').get((req, res) => {

    let uid = req.uid;
    let kind = req.params.kind;

    let projectsArray = [];

    //start with collaborate projects of current user
    db.collection('projects').where("collaborators", 'array-contains', uid).get()
        .then(resp => {
            if (!resp.empty) {
                let others = resp.docs.map((queryDocumentSnapshot => {
                    let project = queryDocumentSnapshot.data();
                    project.id = queryDocumentSnapshot.id;
                    return project;
                }));
                projectsArray = projectsArray.concat(others);
            }

            //here to collect admin projects of current user
            db.collection('projects').where("administrator", "==", uid).get()
                .then(resp => {
                    if (!resp.empty) {
                        let mine = resp.docs.map((queryDocumentSnapshot => {
                            let project = queryDocumentSnapshot.data();
                            project.id = queryDocumentSnapshot.id;
                            return project;
                        }));
                        projectsArray = projectsArray.concat(mine);
                    }

                    console.log("Returning " + projectsArray.length + " projects");

                    if(kind === "deadline"){
                        projectsArray.sort((project1, project2) => {
                            return sortByDateProperty(project1, project2, kind, true)
                        });
                    }
                    else if(kind === "modificationDate"){
                        projectsArray.sort((project1, project2) => {
                            return sortByDateProperty(project1, project2, kind, false)
                        });
                    }
                    else if(kind === "favorite"){
                        projectsArray.filter(project =>  project.isFavorite[uid]);
                    }

                    return res.json({data: projectsArray})

                })
                .catch(err => {
                    console.log(err);
                    return res.status(400).json({ err: err, message: "Error getting projects where user in admin" })
                })

        })
        .catch(err => {
            console.log(err);
            return res.status(400).json({err: err, message: "Error getting projects where user in collaborator"})
        })

});



module.exports = router;
