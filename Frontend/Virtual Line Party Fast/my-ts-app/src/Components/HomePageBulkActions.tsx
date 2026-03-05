import React from "react";
import styles from "../FoodCourt/HomePage.module.css";
import { OrderStatus } from "../Types";

interface HomePageBulkActionsProps {
	hasOrders: boolean;
	allVisibleOrdersSelected: boolean;
	bulkStatusSelection: OrderStatus;
	selectedCount: number;
	statusLabels: Record<OrderStatus, string>;
	onToggleAll: () => void;
	onBulkStatusChange: (status: OrderStatus) => void;
	onApplyBulk: () => void;
}

function HomePageBulkActions({
	hasOrders,
	allVisibleOrdersSelected,
	bulkStatusSelection,
	selectedCount,
	statusLabels,
	onToggleAll,
	onBulkStatusChange,
	onApplyBulk,
}: HomePageBulkActionsProps) {
	return (
		<div className={styles.BulkActionsRow}>
			<button className={styles.PrimaryButton} onClick={onToggleAll} disabled={!hasOrders}>
				{allVisibleOrdersSelected ? "Alle abwählen" : "Alle auswählen"}
			</button>

			<select
				className={styles.StatusSelect}
				value={bulkStatusSelection}
				onChange={(event) => onBulkStatusChange(event.target.value as OrderStatus)}
			>
				{Object.entries(statusLabels).map(([statusKey, label]) => (
					<option key={statusKey} value={statusKey}>
						{label}
					</option>
				))}
			</select>

			<button className={styles.PrimaryButton} onClick={onApplyBulk} disabled={selectedCount === 0}>
				Alle updaten ({selectedCount})
			</button>
		</div>
	);
}

export default HomePageBulkActions;
