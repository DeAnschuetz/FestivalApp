import React, { useEffect, useState } from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";
import ReadyforPickUp from "./ReadyforPickUp";
import InProgress from "./InProgress";
import Orders from "./Orders";
import FoodCourtTab from "./FoodCourtTab";
import { Order } from "../Types";
import Pay from "./Pay";

interface UserProps {}

function User(props: UserProps) {
  const {} = props;
  const [orders, setOrders] = useState<Order[]>([]);
  const [isOrdersOpen, setIsOrdersOpen] = useState(false);
  const [isFoodCourtOpen, setIsFoodCourtOpen] = useState(true);
  const [payisOpen, setPayisOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState("");

  const [credits, setCredits] = useState<number>(0);
  console.log('credits: ', credits);

  useEffect(() => {
    const loginNr = localStorage.getItem("currentUser");
    if (!loginNr) return;

    setCurrentUser(loginNr);

    // Credits laden
    const storedCredits = localStorage.getItem("credits");
    if (storedCredits) {
      const creditsArray: { login_Nr: string; credits: number }[] =
        JSON.parse(storedCredits);
      const userCredit = creditsArray.find((c) => c.login_Nr === loginNr);
      setCredits(userCredit ? userCredit.credits : 0);
    }

    // Orders laden
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

  return (
    <div className={styles.Container}>
      <Header setPayisOpen={setPayisOpen} credits={credits}/>
      {payisOpen ? (
        <Pay
          setPayisOpen={setPayisOpen}
          currentUser={currentUser}
          onCreditsUpdate={(newCredits) => setCredits(newCredits)}
        />
      ) : (
        <>
          <ReadyforPickUp readyForPickup={readyForPickup} />
          <InProgress inProgress={inProgress} />
          <Orders
            orders={orders}
            isFoodCourtOpen={isFoodCourtOpen}
            setIsOrdersOpen={setIsOrdersOpen}
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
