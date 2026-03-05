import React from "react";
import styles from "../FoodCourt/HomePage.module.css";
import { Order, OrderStatus } from "../Types";

interface HomePageOrderCardProps {
	order: Order;
	isSelected: boolean;
	selectedStatus: OrderStatus;
	statusLabels: Record<OrderStatus, string>;
	isOrderStatus: (value: string) => value is OrderStatus;
	onToggleSelected: () => void;
	onStatusChange: (status: OrderStatus) => void;
	onApplyStatus: () => void;
}

function HomePageOrderCard({
	order,
	isSelected,
	selectedStatus,
	statusLabels,
	isOrderStatus,
	onToggleSelected,
	onStatusChange,
	onApplyStatus,
}: HomePageOrderCardProps) {
	const groupedItems = order.orderItems.reduce<Record<string, number>>((accumulator, item) => {
		accumulator[item.displayName] = (accumulator[item.displayName] ?? 0) + item.count;
		console.log('accum', accumulator);
        return accumulator;
        
	}, {});

	const extras = order.orderItems
		.map((item) => item.extra)
		.filter((extraText) => extraText && extraText.trim().length > 0)
		.join(", ");

	return (
		<div className={styles.OrderRow}>
			<label className={styles.SelectionToggle} aria-label="Bestellung auswählen">
				<input type="checkbox" checked={isSelected} onChange={onToggleSelected} />
			</label>
			<div className={styles.OrderCard}>
				<div>
					<div className={styles.OrderTop}>
						<div className={styles.OrderId}>Bestellung #{order.id.slice(0, 8)}</div>
						<div className={styles.OrderMeta}>
							<i className="fa-regular fa-clock" />
							<span>{order.waitingTime} min</span>
						</div>
					</div>

					<div className={styles.Items}>
						{Object.entries(groupedItems).map(([itemName, count]) => (
							<div className={styles.ItemLine} key={itemName}>
								<span>{itemName}</span>
								<span>x {count}</span>
							</div>
						))}
					</div>

					<div className={styles.Extra}>Sonderwünsche: {extras || "-"}</div>
				</div>

				<div className={styles.CardActions}>
					<select
						className={styles.StatusSelect}
						value={selectedStatus}
						onChange={(event) => {
							const value = event.target.value;
							if (!isOrderStatus(value)) {
								return;
							}
							onStatusChange(value);
						}}
					>
						{Object.entries(statusLabels).map(([statusKey, label]) => (
							<option key={statusKey} value={statusKey}>
								{label}
							</option>
						))}
					</select>

					<button className={styles.SmallButton} onClick={onApplyStatus}>
						Status setzen
					</button>
                    {selectedStatus === "READY_FOR_PICKUP" && (
                        <div className={styles.ready}>
                            <i className="fa-regular fa-circle-check" />
                           
                        </div>
                    )}
				</div>
			</div>
		</div>
	);
}

export default HomePageOrderCard;
