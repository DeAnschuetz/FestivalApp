
import { useEffect, useState } from "react";
import { FoodCourt, Product } from "../Api/ffb/types";
import styles from "./Modules/FoodCourtCard.module.css";
import { getProductListByFoodCourtIdFoodCourtId } from "../Api/generated/ffbAPI";
import { getProductsByFoodCourtId } from "../Api/ffb/productApi";
import { getFoodCourtImageUrl } from "../Api/ffb/foodCourtApi";

interface FoodCourtCardProps {
  foodCourt: FoodCourt;
}

function FoodCourtCard(props: FoodCourtCardProps) {
  const [products, setProducts] = useState([] as Product[]) ;
  const [foodCourtImageUrl, setFoodCourtImageUrl] = useState("") ;
  const { foodCourt } = props;

  useEffect(() => {
      const loadProductData = async () => {
        const loginNr = localStorage.getItem("currentUser");
        if (!loginNr) return;
  
        const products: Product[] = await getProductsByFoodCourtId(foodCourt.id);
        const foodCourtImageUrl: string = await getFoodCourtImageUrl(foodCourt.id) ?? "";
        setProducts(products);
        setFoodCourtImageUrl(foodCourtImageUrl);
      };
      

    }, []);


  return (
    <div className={styles.FoodCourtContainer}>
      <div className={styles.FoodCourtName}>{foodCourt.name}</div>
      <div className={styles.WaitingTime}>
        <i className="fa-regular fa-clock"></i>
        {foodCourt.waitingTime} Min
      </div>
      <div className={styles.FlexWrapper}>
        <div className={styles.Img}>
          <img src={foodCourtImageUrl}></img>
        </div>
        <div className={styles.ProductContainer}>
          <div className={styles.Text}>Verfügbar</div>
          <div className={styles.ProductWrapper}>
            {products.map((product) => (
              <div className={styles.ItemContainer}>
                <i className={product.symbolIdentifier}></i>
                <div className={styles.ProductCount}>{product.productCount}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default FoodCourtCard;
