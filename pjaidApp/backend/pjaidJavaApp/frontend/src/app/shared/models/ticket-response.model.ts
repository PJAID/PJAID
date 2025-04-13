import {Device} from './device.model';
import {User} from './user.model';

export interface TicketResponse {
  id: number;
  title: string;
  description: string;
  status: 'NOWE' | 'W_TRAKCIE' | 'ZAMKNIETE';
  device: Device;
  user: User;
}
