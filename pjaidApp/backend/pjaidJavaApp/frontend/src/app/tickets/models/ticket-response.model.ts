export interface TicketResponse {
  id: number;
  title: string;
  description: string;
  status: 'NOWE' | 'W_TRAKCIE' | 'ZAMKNIETE';
}
