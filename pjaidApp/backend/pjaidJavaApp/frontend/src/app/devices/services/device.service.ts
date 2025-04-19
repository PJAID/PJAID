import {inject} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DeviceResponse} from '../../shared/models/device-response.model';
import {environment} from '../../../environments/environment';

export class DeviceService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/devices`;

  getDevices(filters?: any): Observable<DeviceResponse[]> {
    let params = new HttpParams();
    if (filters) {
      Object.keys(filters).forEach(key => {
        const value = filters[key];
        if (value !== null && value !== undefined && value !== '') {
          params = params.append(key, value);
        }
      });
    }
    return this.http.get<DeviceResponse[]>(this.baseUrl, {params});
  }

  getDeviceById(id: number): Observable<DeviceResponse> {
    return this.http.get<DeviceResponse>(`${this.baseUrl}/${id}`);
  }

  addDevice(dto: Partial<DeviceResponse>): Observable<DeviceResponse> {
    return this.http.post<DeviceResponse>(`${this.baseUrl}`, dto);
  }
}
