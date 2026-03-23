import React, { useState } from "react";
import { BasketItem, Order } from "../Types";
import styles from "./Modules/ShoppingBasket.module.css";
import { Button } from "@progress/kendo-react-buttons";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";

interface ShoppingBasketProps {
  orderBasket: {
    foodcourt_name: string;
    waiting_time: number;
    Items: BasketItem[];
  };
  credits: number;
  setCredits: React.Dispatch<React.SetStateAction<number>>;
  setOpenBasket: React.Dispatch<React.SetStateAction<boolean>>;
  setOrderBasket: React.Dispatch<
    React.SetStateAction<{
      foodcourt_name: string;
      Items: BasketItem[];
      waiting_time: number;
    }>
  >;
  orders: Order[];
  setOrders: React.Dispatch<React.SetStateAction<Order[]>>;
  currentUser: string;
}

function ShoppingBasket(props: ShoppingBasketProps) {
  const {
    orderBasket,
    setOpenBasket,
    setOrderBasket,
    credits,
    setCredits,
    setOrders,
    currentUser,
  } = props;
  const [showErrorText, setShowErrorText] = useState(false);

  const totalPrice = orderBasket.Items.reduce((sum, item) => {
    return sum + item.price * item.amount;
  }, 0);

  const handleOrder = () => {
    if (orderBasket.Items.length === 0) return;

    const totalPrice = orderBasket.Items.reduce((sum, item) => {
      return sum + item.price * item.amount;
    }, 0);

    //  Zu wenig Credits
    if (credits < totalPrice) {
      setShowErrorText(true);
      return;
    }

    //  Credits aktualisieren
    const newCredits = credits - totalPrice;
    setCredits(newCredits);

    //  Credits im localStorage updaten
    const storedCredits = JSON.parse(localStorage.getItem("credits") || "[]");

    const loginNr = currentUser;

    const updatedCredits = storedCredits.map((c: any) =>
      c.login_Nr === loginNr ? { ...c, credits: newCredits } : c,
    );

    localStorage.setItem("credits", JSON.stringify(updatedCredits));

    // Orders aus localStorage holen
    const storedOrders = JSON.parse(localStorage.getItem("orders") || "[]");

    // Neue Order
    const newOrder = {
      loginNr: loginNr,
      order_number: "#" + Math.floor(Math.random() * 100000),
      foodcourt: orderBasket.foodcourt_name,
      order_status: "in_progress",
      waiting_time: orderBasket.waiting_time,
      orderItems: orderBasket.Items.map((item) => ({
        name: item.name,
        count: item.amount,
        subItems: item.subItems || [],
        extra: item.extra || "",
      })),
    };

    //  Order speichern
    const updatedOrders = [...storedOrders, newOrder];
    console.log("updatedOrders: ", updatedOrders);
    localStorage.setItem("orders", JSON.stringify(updatedOrders));
    setOrders(updatedOrders.filter((order) => order.loginNr === loginNr));

    // Warenkorb leeren
    setOrderBasket({
      foodcourt_name: "",
      Items: [],
      waiting_time: 0,
    });

    setOpenBasket(false);

    alert("Bestellung erfolgreich!");
  };

  return (
    <div className={styles.BasketWrapper}>
      <div className={styles.FlexWrapper}>
        <div className={styles.Title}>Warenkorb</div>
        <Button
          type="button"
          fillMode="flat"
          iconClass="fa-regular fa-circle-xmark"
          size="small"
          style={{ fontSize: "24px" }}
          onClick={() => setOpenBasket(false)}
        />
      </div>
      <div className={styles.OrderWrapper}>
        {orderBasket.Items.length === 0 ? (
          <div className={styles.Empty}>Warenkorb ist leer</div>
        ) : (
          orderBasket.Items.map((item, index) => (
            <div key={index} className={styles.ItemRow}>
              <div className={styles.FlexWrapper}>
                <div className={styles.ItemName}>- {item.name}</div>
                <div className={styles.Price}>{item.price} €</div>

                <InputElement
                  label=""
                  editorId="anzahl"
                  value={item.amount}
                  onChange={() => console.log("gu")}
                  labelStyle={{ width: "100%", color: "black", padding: 0 }}
                  inputStyle={{ color: "black" }}
                  wrapperStyle={{ margin: "0px 6px 0px 0px", width: "40px" }}
                />

                <Button
                  type="button"
                  fillMode={"flat"}
                  iconClass="fa-regular fa-trash-can"
                  size={"medium"}
                  onClick={() =>
                    setOrderBasket((prev) => ({
                      ...prev,
                      Items: prev.Items.filter((_, i) => i !== index),
                    }))
                  }
                />
              </div>

              {item.extra && <div className={styles.Extra}>| {item.extra}</div>}

              {item.subItems?.map((subItem, subIndex) => (
                <div key={subIndex} className={styles.SubItem}>
                  - {subItem.name}
                </div>
              ))}
            </div>
          ))
        )}
      </div>
      <div className={styles.Line}></div>
      <div className={styles.Total}>
        <div className={styles.Text}>Bestellwert</div>
        <div>{totalPrice} €</div>
      </div>
      <div className={`${styles.Flex} ${styles.Disabled}`}>
        <Checkbox style={{ marginRight: "12px" }} disabled={true} />
        Bestellung priorisiert behandeln
      </div>
      <div className={styles.WaitingTimeWrapper}>
        <div>Geschätzte Zubereitungszeit</div>
        <div className={styles.WaitingTime}>
          <i className="fa-regular fa-clock"></i>
          <div className={styles.Time}>
            {orderBasket.Items.length === 0 ? 0 : orderBasket.waiting_time} Min
          </div>
        </div>
      </div>
      <div className={styles.OrderBtnWrapper}>
        <Button className={styles.OrderBtn} onClick={handleOrder}>
          Jetzt Bestellen
        </Button>
      </div>
      <div className={styles.Line}></div>
      {showErrorText && (
        <div className={styles.ErrorText}>
          Zu wenig Credits. Bitte Credits aufladen um Fortzufahren
        </div>
      )}
    </div>
  );
}

export default ShoppingBasket;
