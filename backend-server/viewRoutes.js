const express = require("express");
const router = express.Router();
const mongoose = require("mongoose");
const userdata = require("./models/userData");
const { find, findById } = require("./models/userData");
const place = require("./models/place");

router.get("/", function(req, res, next) {

  userdata.find({},function(err,data){
    res.render('showUserData.ejs',{
      userdata : data
    });
  })
});



module.exports = router;
