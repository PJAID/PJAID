import {inject} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Device} from '../../shared/models/device.model';

export class DeviceService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/devices';

  getDevices(filters?: any): Observable<Device[]> {
    let params = new HttpParams();
    if (filters) {
      Object.keys(filters).forEach(key => {
        const value = filters[key];
        if (value !== null && value !== undefined && value !== '') {
          params = params.append(key, value);
        }
      });
    }
    return this.http.get<Device[]>(this.baseUrl, { params });
  }

  getDeviceById(id: number): Observable<Device> {
    return this.http.get<Device>(`${this.baseUrl}/${id}`);
  }

}
