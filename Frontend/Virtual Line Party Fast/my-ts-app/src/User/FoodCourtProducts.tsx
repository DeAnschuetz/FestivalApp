import React, { useEffect, useState } from "react";
import styles from "./Modules/FoodCourtProducts.module.css";
import { BasketItem  } from "../Types";
import { Button } from "@progress/kendo-react-buttons";
import AddOrderItem from "./AddOrderItem";
import { Cart, FoodCourt, Product } from "../Api/ffb/types";
import { getProductsByFoodCourtId } from "../Api/ffb/productApi";

interface FoodCourtProductsProps {
  currentFoodCourt: FoodCourt | undefined;
  setClickedFoodCourt: React.Dispatch<React.SetStateAction<string>>;
  setCart: React.Dispatch<React.SetStateAction<Cart>>;
  cart: Cart;
}

function FoodCourtProducts(props: FoodCourtProductsProps) {
  const { currentFoodCourt, setClickedFoodCourt, setCart, cart } =
    props;
  console.log("orderBasket: ", cart);
  const [openAddItemDialog, setOpenAddItemDialog] = useState(false);
  const [clickedItem, setClickedItem] = useState({});
  const [errorText, setErrorText] = useState("");
  const [products, setProducts] = useState<Product[]>([]);

  const onAddClick = (item: any) => {
    setClickedItem(item);
    setOpenAddItemDialog(true);
    setErrorText("");
  };

  useEffect(() => {
      const loadProductData = async () => {
        try {
          if (currentFoodCourt) {
            const products: Product[] = await getProductsByFoodCourtId(currentFoodCourt?.id);
            setProducts(products);
          }
        } catch (error) {
          
        } 
      };
    
      void loadProductData();
  }, []);

  return (
    <div className={styles.FoodCourtProducts}>
      <div className={styles.FlexWrapper}>
        <div className={styles.CourtName}>{currentFoodCourt?.name}</div>
        <Button
          type="button"
          fillMode="flat"
          iconClass="fa-regular fa-circle-xmark"
          size="small"
          style={{ fontSize: "24px" }}
          onClick={() => setClickedFoodCourt("")}
        />
      </div>
      <div className={styles.ProductsContainer}>
        {products.map((product) => (
          <div>
            <div className={styles.ProductRow}>
              <div className={styles.Wrapper}>
                <i className={product.symbolIdentifier}></i>
                <div className={styles.Name}>{product.displayName}</div>
              </div>
              <div className={styles.Wrapper}>
                <div className={styles.Price}>{product.price} €</div>
                <Button
                  type="button"
                  fillMode={"outline"}
                  iconClass="fa fa-plus"
                  style={{ borderRadius: "100%" }}
                  size={"small"}
                  onClick={() => onAddClick(product)}
                />
              </div>
            </div>
            <div className={styles.SubItemsWrapper}>
              {product.subProducts?.map((item) => (
                <div>- {item.displayName}</div>
              ))}
            </div>
          </div>
        ))}
      </div>
      {errorText && (
        <div className={styles.ErrorText}>
          <b>Bestellung im Warenkorb zuerst abschließen. </b> Parallel bei
          unterschiedlichen Ständen bestellen ist nicht möglich
        </div>
      )}
      <div className={styles.WaitingTimeContainer}>
        <div>Geschätzte Wartezeit</div>
        <div className={styles.WaitingTime}>
          <i className="fa-regular fa-clock"></i>
          <div className={styles.Time}>
            {currentFoodCourt?.waitingTime} Min
          </div>
        </div>
      </div>
      {openAddItemDialog && (
        <AddOrderItem
          products={products}
          clickedItem={clickedItem}
          setOpenAddItemDialog={setOpenAddItemDialog}
          currentFoodCourt={currentFoodCourt}
          setCart={setCart}
        />
      )}
    </div>
  );
}

export default FoodCourtProducts;
