const express = require("express");
const mongoose = require("mongoose");
const viewroutes = require("./viewRoutes.js");
const apiroutes = require("./apiRoutes.js");

var app = express();

// Configure view engine to render EJS templates.
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);


mongoose
  .connect("mongodb://localhost:27017/gpbl_project", { useNewUrlParser: true, useUnifiedTopology: true})
  .then(() => {
    app.use(express.json());
    app.use("/",viewroutes);
    app.use("/api", apiroutes);
    

    // you can change port
    app.listen(4500, () => {
      console.log("Server has started!");
    });
  });
