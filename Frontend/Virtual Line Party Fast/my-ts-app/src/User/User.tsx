import React, { useEffect, useState } from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";
import ReadyforPickUp from "./ReadyforPickUp";
import InProgress from "./InProgress";
import Orders from "./Orders";
import FoodCourtTab from "./FoodCourtTab";
import { BasketItem, FoodCourt, Order } from "../Types";
import Pay from "./Pay";
import ViewOrder from "./ViewOrder";
import FoodCourtProducts from "./FoodCourtProducts";
import ShoppingBasket from "./ShoppingBasket";

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
  const [foodCourts, setFoodCourts] = useState<FoodCourt[]>([]);
  const [clickedFoodCourt, setClickedFoodCourt] = useState("");
  const [orderBasket, setOrderBasket] = useState<{
    foodcourt_name: string;
    waiting_time: number;
    Items: BasketItem[];
  }>({
    foodcourt_name: "",
    Items: [],
    waiting_time: 0,
  });
  const [openBasket, setOpenBasket] = useState(false);

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

  useEffect(() => {
    const stored = localStorage.getItem("foodcourts");
    if (stored) {
      setFoodCourts(JSON.parse(stored));
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

  const currentFoodcourt = foodCourts.find(
    (court: FoodCourt) => court.name === clickedFoodCourt,
  );

  return (
    <div className={styles.Container}>
      <Header
        setPayisOpen={setPayisOpen}
        credits={credits}
        setOpenBasket={setOpenBasket}
        openBasket={openBasket}
        orderBasket={orderBasket}
        setClickedFoodCourt={setClickedFoodCourt}
      />
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
      ) : clickedFoodCourt !== "" ? (
        <FoodCourtProducts
          currentFoodCourt={currentFoodcourt}
          setClickedFoodCourt={setClickedFoodCourt}
          setOrderBasket={setOrderBasket}
          orderBasket={orderBasket}
        />
      ) : openBasket ? (
        <ShoppingBasket
          orderBasket={orderBasket}
          setOpenBasket={setOpenBasket}
          setOrderBasket={setOrderBasket}
          credits={credits}
          setCredits={setCredits}
          orders = {orders}
          setOrders={setOrders}
          currentUser={currentUser}
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
            foodCourts={foodCourts}
            setClickedFoodCourt={setClickedFoodCourt}
          />
        </>
      )}
    </div>
  );
}

export default User;
