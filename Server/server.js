const express = require('express');
const cors = require('cors');

const PORT = process.env.PORT || 8080;




// App
const app = express();

app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
    res.send('Hello world\n');
});

const usersRouter = require('./routes/users');
// const groupRouter = require('./routes/groups');
// const projectRouter = require('./routes/projects')

app.use('/', usersRouter);
// app.use('/', groupRouter);
// app.use('/',projectRouter)


app.listen(PORT, ()=>{console.log(`Running on http://localhost:${PORT}`);});

module.exports = app;
