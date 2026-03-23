import React, { useEffect, useState } from "react";
import { BasketItem } from "../Types";
import styles from "./Modules/ShoppingBasket.module.css";
import { Button } from "@progress/kendo-react-buttons";
import InputElement from "../Components/InputElement";
import { Checkbox } from "@progress/kendo-react-inputs";
import { Cart, CartItem, Order } from "../Api/ffb/types";
import { apiGetCart, apiRemoveCartItem } from "../Api/ffb/cartApi";
import { createFoodOrder, getVisibleOrders } from "../Api/ffb";

interface ShoppingBasketProps {
  cart: Cart;
  credits: number;
  setCredits: React.Dispatch<React.SetStateAction<number>>;
  setCartOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setCartOutside: React.Dispatch<
    React.SetStateAction<Cart>
  >;
  orders: Order[];
  setOrders: React.Dispatch<React.SetStateAction<Order[]>>;
  currentUser: string;
}

function ShoppingBasket(props: ShoppingBasketProps) {
  const {
    cart,
    setCartOpen,
    setCartOutside,
    credits,
    setCredits,
    setOrders,
    currentUser,
  } = props;
  const [showErrorText, setShowErrorText] = useState(false);
  const [cartLocal, setCart] = useState<Cart>();

  useEffect(() => {
    const loadCartData = async () => {
      try {
        const cart: Cart = await apiGetCart();
        setCart(cart);
        setCartOutside(cart);
      } catch (error) {

      }
    };

    void loadCartData();
  }, []);

  const removeCartItem = (cartItemId: string) => {
    const removeCartItemLocal = async () => {
      try {
        const newCart: Cart= await apiRemoveCartItem(cartItemId);
        setCart(newCart);
      } catch (error) {

      }
    };
    removeCartItemLocal();
  }


  const handleOrder = async () => {
    const cartItems: CartItem[] = cart.cartItems ?? [];
    if (cartItems.length === 0) return;

    try {
      const a: Order[] = await createFoodOrder();
      setCartOutside(cartLocal ?? {} as Cart);
  
      setCartOpen(false);
  
      alert("Bestellung erfolgreich!");
    } catch (error) {
      
    }
  };

  
  const cartItems: CartItem[] = cart.cartItems ?? [];

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
          onClick={() => setCartOpen(false)}
        />
      </div>
      <div className={styles.OrderWrapper}>
        {cartItems.length === 0 ? (
          <div className={styles.Empty}>Warenkorb ist leer</div>
        ) : (
          cartItems.map((item, index) => (
            <div key={index} className={styles.ItemRow}>
              <div className={styles.FlexWrapper}>
                <div className={styles.ItemName}>- {item.displayName}</div>
                <div className={styles.Price}>{item.price} €</div>

                <InputElement
                  label=""
                  editorId="anzahl"
                  value={item.count}
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
                  onClick={() => removeCartItem(item.id)}
                />
              </div>

              {item.extra && <div className={styles.Extra}>| {item.extra}</div>}

              {item.subItems?.map((subItem, subIndex) => (
                <div key={subIndex} className={styles.SubItem}>
                  - {subItem.displayName}
                </div>
              ))}
            </div>
          ))
        )}
      </div>
      <div className={styles.Line}></div>
      <div className={styles.Total}>
        <div className={styles.Text}>Bestellwert</div>
        <div>{cart?.total} €</div>
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
            {cartLocal?.cartItems.length === 0 ? 0 : 0} Min
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
