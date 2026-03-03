import React from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";

interface Props {}

function User(props: Props) {
  const {} = props;

  // const response = fetch("http://10.45.129.22:8080/food_order/list_all", {
  //   method: "Get",
  //   headers: {
  //     "Content-Type": "application/json",
  //   },
  //   credentials: "include",
  //   // body: JSON.stringify({ loginNr, password }),
  // });

  // console.log("hier", response);

  return (
    <div>
      <Header />
    </div>
  );
}

export default User;
