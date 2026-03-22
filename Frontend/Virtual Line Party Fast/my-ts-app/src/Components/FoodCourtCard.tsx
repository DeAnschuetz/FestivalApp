import React from "react";
import { FoodCourt } from "../Types";
import styles from "./Modules/FoodCourtCard.module.css";

interface FoodCourtCardProps {
  foodCourt: FoodCourt;
  setClickedFoodCourt: React.Dispatch<React.SetStateAction<string>>
}

function FoodCourtCard(props: FoodCourtCardProps) {
  const { foodCourt, setClickedFoodCourt } = props;

  const productsWithoutType = foodCourt.products.filter(
    (product) => product.type !== 'Menue',
  );

  return (
    <div className={styles.FoodCourtContainer} onClick={()=>setClickedFoodCourt(foodCourt.name)}>
      <div className={styles.FoodCourtName}>{foodCourt.name}</div>
      <div className={styles.WaitingTime}>
        <i className="fa-regular fa-clock"></i>
        {foodCourt.avg_waiting_time} Min
      </div>
      <div className={styles.FlexWrapper}>
        <div className={styles.Img}>
          <img src={foodCourt.imageUrl}></img>
        </div>
        <div className={styles.ProductContainer}>
          <div className={styles.Text}>Verfügbar</div>
          <div className={styles.ProductWrapper}>
            {productsWithoutType.map((product) => (
              <div className={styles.ItemContainer}>
                <i className={product.icon}></i>
                <div className={styles.ProductCount}>{product.count}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default FoodCourtCard;
