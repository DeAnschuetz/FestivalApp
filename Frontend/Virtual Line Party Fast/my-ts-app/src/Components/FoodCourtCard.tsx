
import { useEffect, useState } from "react";
import { FoodCourt, Product } from "../Api/ffb/types";
import styles from "./Modules/FoodCourtCard.module.css";
import { getProductsByFoodCourtId } from "../Api/ffb/productApi";
import { getFoodCourtImageUrl } from "../Api/ffb/foodCourtApi";

interface FoodCourtCardProps {
  foodCourt: FoodCourt;
  setClickedFoodCourt: React.Dispatch<React.SetStateAction<string>>;
  
}

function FoodCourtCard(props: FoodCourtCardProps) {
  const { foodCourt, setClickedFoodCourt } = props;
  const [products, setProducts] = useState([] as Product[]) ;
  const [foodCourtImageUrl, setFoodCourtImageUrl] = useState("") ;
  
  useEffect(() => {
      const loadProductData = async () => {
        try {
          const loginNr = localStorage.getItem("currentUser");
          if (!loginNr) return;
          console.log(foodCourt);
          const products: Product[] = await getProductsByFoodCourtId(foodCourt.id);
          console.log("products: ", products);
          setProducts(products);
          const foodCourtImageUrl: string = await getFoodCourtImageUrl(foodCourt.id) ?? "";
          setFoodCourtImageUrl(foodCourtImageUrl);
        } catch (error) {
          
        }
      };
      
      void loadProductData();
    }, []);


  return (
    <div
      className={styles.FoodCourtContainer}
      onClick={() => setClickedFoodCourt(foodCourt.id)}
    >
      <div className={styles.FoodCourtName}>{foodCourt.name}</div>
      <div className={styles.WaitingTime}>
        <i className="fa-regular fa-clock"></i>
        {foodCourt.waitingTime} Min
      </div>
      <div className={styles.FlexWrapper}>
        <div className={styles.Img}>
          {foodCourtImageUrl.length > 0 ? <img src={foodCourtImageUrl}></img> : <></>} 
        </div>
        <div className={styles.ProductContainer}>
          <div className={styles.Text}>Verfügbar</div>
          <div className={styles.ProductWrapper}>
            {products
                .filter((product) => {
                  return product.subProducts.length == 0
                })
                .map((product) => (
                  <div className={styles.ItemContainer}>
                    <i className={product.symbolIdentifier}></i>
                    <div className={styles.ProductCount}>{product.productCount}</div>
                  </div>
                ))
            }
          </div>
        </div>
      </div>
    </div>
  );
}

export default FoodCourtCard;
