import React, { useEffect, useState } from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";
import ReadyforPickUp from "./ReadyforPickUp";
import InProgress from "./InProgress";
import Orders from "./Orders";
import FoodCourtTab from "./FoodCourtTab";
import { Order } from "../Types";
import Pay from "./Pay";
import ViewOrder from "./ViewOrder";

interface UserProps {}

function User(props: UserProps) {
  const {} = props;
  const [orders, setOrders] = useState<Order[]>([]);
  const [isOrdersOpen, setIsOrdersOpen] = useState(false);
  const [isFoodCourtOpen, setIsFoodCourtOpen] = useState(true);
  const [payisOpen, setPayisOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState("");
  const [credits, setCredits] = useState<number>(0);
  const [clickedCrad, setClickedCard] = useState("");
  console.log("clickedCrad: ", clickedCrad);

  useEffect(() => {
    const loginNr = localStorage.getItem("currentUser");
    if (!loginNr) return;

    setCurrentUser(loginNr);

    const storedCredits = localStorage.getItem("credits");
    if (storedCredits) {
      const creditsArray: { login_Nr: string; credits: number }[] =
        JSON.parse(storedCredits);
      const userCredit = creditsArray.find((c) => c.login_Nr === loginNr);
      setCredits(userCredit ? userCredit.credits : 0);
    }

    const storedOrders = localStorage.getItem("orders");
    if (storedOrders) {
      const allOrders: Order[] = JSON.parse(storedOrders);
      setOrders(allOrders.filter((order) => order.loginNr === loginNr));
    }
  }, []);

  const readyForPickup = orders.filter(
    (order: Order) => order.order_status === "ready_for_pickup",
  );

  const inProgress = orders.filter(
    (order: Order) => order.order_status === "in_progress",
  );

  const currentOrder = orders.find(
    (order: Order) => order.order_number === clickedCrad,
  );

  return (
    <div className={styles.Container}>
      <Header setPayisOpen={setPayisOpen} credits={credits} />
      {payisOpen ? (
        <Pay
          setPayisOpen={setPayisOpen}
          currentUser={currentUser}
          onCreditsUpdate={(newCredits) => setCredits(newCredits)}
        />
      ) : clickedCrad !== "" ? (
        <ViewOrder
          currentOrder={currentOrder}
          setClickedCard={setClickedCard}
        />
      ) : (
        <>
          <ReadyforPickUp
            readyForPickup={readyForPickup}
            setClickedCard={setClickedCard}
          />
          <InProgress inProgress={inProgress} setClickedCard={setClickedCard} />
          <Orders
            orders={orders}
            isFoodCourtOpen={isFoodCourtOpen}
            setIsOrdersOpen={setIsOrdersOpen}
            setClickedCard={setClickedCard}
          />
          <FoodCourtTab
            isOrdersOpen={isOrdersOpen}
            setIsFoodCourtOpen={setIsFoodCourtOpen}
          />
        </>
      )}
    </div>
  );
}

export default User;
