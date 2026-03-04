export type OrderStatus = 'READY_FOR_PICKUP' | 'IN_PROGRESS';

export interface OrderItem {
  productID: string;
  displayName: string;
  iconIdentifier: string;
  count: number;
  extra: string;
  subItems?: any[];
}

export interface Order {
  id: string;
  status: OrderStatus;
  foodCourtName: string;
  waitingTime: number;
  orderItems: OrderItem[];
}