import React from "react";
import { FoodCourt } from "../Types";
import styles from "./Modules/FoodCourtCard.module.css";

interface FoodCourtCardProps {
  foodCourt: FoodCourt;
}

function FoodCourtCard(props: FoodCourtCardProps) {
  const { foodCourt } = props;

  const productsWithoutType = foodCourt.products.filter(
    (product) => !product.type,
  );

  return (
    <div className={styles.FoodCourtContainer}>
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
