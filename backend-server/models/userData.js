const mongoose = require("mongoose");

const schema = mongoose.Schema({
  userName: String,
  userPhone: String,
  userEmail : String,
  UUID : String,
  Major : String,
  Minor : String,
  timeData : String,
  placeName : String
},{
  versionKey: false 
}
);

module.exports = mongoose.model("userData", schema);
