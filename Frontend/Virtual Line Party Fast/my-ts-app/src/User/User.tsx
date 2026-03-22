import React, { useEffect, useState } from "react";
import styles from "./Modules/User.module.css";
import Header from "./Header";
import ReadyforPickUp from "./ReadyforPickUp";
import InProgress from "./InProgress";
import FoodCourtTab from "./FoodCourtTab";
import { BasketItem, FoodCourt, Order } from "../Types";
import Pay from "./Pay";
import ViewOrder from "./ViewOrder";
import { Credit, Order } from "../Api/ffb/types";
import { getCredit } from "../Api/ffb/creditApi";
import { getVisibleOrders } from "../Api/ffb/foodOrderApi";
import { FoodOrderStatus as OrderStatus  } from "../Api/generated/ffbAPI.schemas";
import Orders from "./Orders";
import FoodCourtProducts from "./FoodCourtProducts";

interface UserProps {}

function User(props: UserProps) {
  const {} = props;
  const [orders, setOrders] = useState<Order[]>([]);
  const [isOrdersOpen, setIsOrdersOpen] = useState(false);
  const [isFoodCourtOpen, setIsFoodCourtOpen] = useState(true);
  const [payisOpen, setPayisOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState("");
  const [credits, setCredits] = useState<number>(0);
  const [clickedCard, setClickedCard] = useState("");
  const [foodCourts, setFoodCourts] = useState<FoodCourt[]>([]);
  const [clickedFoodCourt, setClickedFoodCourt] = useState("");
  const [orderBasket, setOrderBasket] = useState<{
    foodcourt_name: string;
    Items: BasketItem[];
  }>({
    foodcourt_name: "",
    Items: [],
  });
  console.log("orderBasket: ", orderBasket);
  const [clickedCard, setClickedCard] = useState("");
  console.log("clickedCard: ", clickedCard);


  useEffect(() => {
    const loadUserData = async () => {
      const loginNr = localStorage.getItem("currentUser");
      if (!loginNr) return;
      setCurrentUser(loginNr);

      const credit: Credit = await getCredit();
      setCredits(credit.credit);

      const order: Order[] = await getVisibleOrders();
      setOrders(order);
    };

    void loadUserData();
  }, []);

  useEffect(() => {
    const stored = localStorage.getItem("foodcourts");
    if (stored) {
      setFoodCourts(JSON.parse(stored));
    }
  }, []);

  const readyForPickup = orders.filter(
    (order: Order) => order.status === OrderStatus.READY_FOR_PICKUP,
  );

  const inProgress = orders.filter(
    (order: Order) => order.status === OrderStatus.IN_PROGRESS,
  );

  const currentOrder = orders.find(
    (order: Order) => order.id === clickedCard,
  );

  const currentFoodcourt = foodCourts.find(
    (court: FoodCourt) => court.name === clickedFoodCourt,
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
      ) : clickedCard !== "" ? (
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
