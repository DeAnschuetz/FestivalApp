import styles from "./Modules/ReadyforPickUp.module.css";
import OrderCard from "../Components/OrderCard";
import { Order } from "../Api/ffb/types";

interface Props {
  readyForPickup: Order[];
  setClickedCard: React.Dispatch<React.SetStateAction<string>>
}

function ReadyforPickUp(props: Props) {
  const { readyForPickup, setClickedCard } = props;

  return (
    <div className={styles.AbholbereitContainer}>
      <div className={styles.AbholbereitTitleWrapper}>
        <i className="fa fa-clipboard-list"></i>
        <div className={styles.AbholbereitTitle}>Abholbereit</div>
      </div>
      <div className={styles.OrderWrapper}>
        {readyForPickup.map((order) => (
          <OrderCard order={order} status="ready_for_pickup" setClickedCard = {setClickedCard}/>
        ))}
      </div>
    </div>
  );
}

export default ReadyforPickUp;
