export interface TicketRequest {
  title: string;
  description: string;
  status: 'NOWE' | 'W_TRAKCIE' | 'ZAMKNIETE';
  deviceId: number;
  userId: number;
}
