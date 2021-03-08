const express = require("express");
const Post = require("./models/userData");
const mongoose = require("mongoose");
const { find, findById } = require("./models/userData");
const place = require("./models/place");
const router = express.Router();

mongoose.connect("mongodb://localhost:27017/gpbl_project", { useNewUrlParser: true, useUnifiedTopology: true})


router.get("/posts", async (req, res) => {
  const posts = await Post.find();
  res.send(posts);
});

router.post("/posts", async (req, res) => {
  var sendQuery = new Object();
  sendQuery.UUID = req.body.UUID;
  sendQuery.Major = req.body.Major;
  sendQuery.Minor = req.body.Minor;

  JSON.stringify(sendQuery);

  var userPlace;
  
  await place.findOne(sendQuery,function(err,data){
    userPlace = String(data.placeName);
  });

  const post = new Post({
    userName: req.body.userName,
  userPhone: req.body.userPhone,
  userEmail : req.body.userEmail,
  UUID : req.body.UUID,
  Major : req.body.Major,
  Minor : req.body.Minor,
  timeData : req.body.timeData,
  placeName : userPlace
  });

  console.log(post);

  await post.save();
  console.log(userPlace);

  res.send(post);
});

module.exports = router;
