import React, { useState } from "react";
import styles from "../FoodCourt/HomePage.module.css";
import { OrderStatus } from "../Types";

interface HomePageBulkActionsProps {
	hasOrders: boolean;
	allVisibleOrdersSelected: boolean;
	selectedCount: number;
	statusLabels: Record<OrderStatus, string>;
	onToggleAll: () => void;
	onApplyBulk: (status: OrderStatus) => void;
}

function HomePageBulkActions({
	hasOrders,
	allVisibleOrdersSelected,
	selectedCount,
	statusLabels,
	onToggleAll,
	onApplyBulk,
}: HomePageBulkActionsProps) {
	const [selectedBulkValue, setSelectedBulkValue] = useState<string>("");

	return (
		<div className={styles.BulkActionsRow}>
			<label className={styles.BulkSelectAll} aria-label="Alle Bestellungen auswählen">
				<input
					type="checkbox"
					checked={allVisibleOrdersSelected}
					onChange={onToggleAll}
					disabled={!hasOrders}
				/>
				<span>Alle</span>
			</label>

			<select
				className={styles.BulkUpdateSelect}
				value={selectedBulkValue}
				onChange={(event) => {
					const value = event.target.value as OrderStatus | "";
					setSelectedBulkValue(value);

					if (!value) {
						return;
					}

					onApplyBulk(value);
					setSelectedBulkValue("");
				}}
				disabled={selectedCount === 0}
			>
				<option value="">Alle Updaten</option>
				{Object.entries(statusLabels).map(([statusKey, label]) => (
					<option key={statusKey} value={statusKey}>
						{label}
					</option>
				))}
			</select>
		</div>
	);
}

export default HomePageBulkActions;
