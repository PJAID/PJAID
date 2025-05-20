import {DeviceResponse} from './device-response.model';
import {User} from './user.model';

export interface TicketResponse {
  id: number;
  title: string;
  description: string;
  status: 'NOWE' | 'W_TRAKCIE' | 'ZAMKNIETE';
  device: DeviceResponse;
  user: User;

  technicianId?: number;
  technicianName?: string;
}
