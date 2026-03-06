import React from "react";
import styles from "../FoodCourt/HomePage.module.css";

interface FilterOption {
	key: string;
	label: string;
}

interface HomePageFilterBarProps {
	filters: FilterOption[];
	activeFilter: string;
	onSelectFilter: (filterKey: string) => void;
	getFilterCount: (filterKey: string) => number;
}

function HomePageFilterBar({
	filters,
	activeFilter,
	onSelectFilter,
	getFilterCount,
}: HomePageFilterBarProps) {
	return (
		<div className={styles.FilterRow}>
			{filters.map((filter) => (
				<button
					key={filter.key}
					className={`${styles.FilterButton} ${activeFilter === filter.key ? styles.FilterActive : ""}`}
					onClick={() => onSelectFilter(filter.key)}
				>
					{filter.label} ({getFilterCount(filter.key)})
				</button>
			))}
		</div>
	);
}

export default HomePageFilterBar;
