const mongoose = require("mongoose");

const schema = mongoose.Schema({
  UUID : String,
  Major : String,
  Minor : String,
  placeName : String
},{
  versionKey: false 
}
);

module.exports = mongoose.model("place", schema);
