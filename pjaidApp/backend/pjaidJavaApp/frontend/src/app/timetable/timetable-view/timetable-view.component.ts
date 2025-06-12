import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Technician {
  id: string;
  name: string;
  surname: string;
  isPresent: boolean;
}

@Component({
  selector: 'app-timetable-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './timetable-view.component.html',
  styleUrls: ['./timetable-view.component.css']
})
export class TimetableViewComponent implements OnInit {
  selectedDate: string = new Date().toISOString().split('T')[0];
  technicians: Technician[] = [];
  userIsAdmin: boolean = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fakeLogin();
    this.loadTechniciansFromXML();
  }

  fakeLogin(): void {
    const username = 'admin';
    const password = 'admin';

    if (username === 'admin' && password === 'admin') {
      this.userIsAdmin = true;
    }
  }

  loadTechniciansFromXML(): void {
    this.http.get('shift_schedule.xml', { responseType: 'text' }).subscribe((xmlString: string) => {
      const parser = new DOMParser();
      const xml = parser.parseFromString(xmlString, 'application/xml');
      const entries = Array.from(xml.getElementsByTagName('entry'));

      const now = new Date();
      const currentDate = now.toISOString().split('T')[0];
      const currentHour = now.getHours();

      const map = new Map<string, Technician>();

      let presentCount = 0;
      entries.forEach(entry => {
        const name = entry.querySelector('name')?.textContent?.trim() || '';
        const surname = entry.querySelector('surname')?.textContent?.trim() || '';
        const date = entry.querySelector('date')?.textContent?.trim() || '';
        const timeFrom = entry.querySelector('timeFrom')?.textContent?.trim() || '';
        const timeTo = entry.querySelector('timeTo')?.textContent?.trim() || '';

        const from = parseInt(timeFrom.split(':')[0], 10);
        const to = parseInt(timeTo.split(':')[0], 10);
        const hour = currentHour;

        // aktualna zmiana zaciągnięta z pliku
        const isOnShift =
          date === currentDate &&
          ((from < to && hour >= from && hour < to) || (from > to && (hour >= from || hour < to)));

        const id = name + '_' + surname;

        let isPresent = false;
        if (!map.has(id)) {
          if (isOnShift && presentCount < 3) {
            isPresent = true;
            presentCount++;
          }
          map.set(id, {
            id,
            name,
            surname,
            isPresent
          });
        } else if (isOnShift && presentCount < 3) {
          map.get(id)!.isPresent = true;
          presentCount++;
        }
      });

      this.technicians = Array.from(map.values());
    });
  }

  togglePresence(tech: Technician): void {
    if (this.userIsAdmin) {
      tech.isPresent = !tech.isPresent;
    }
  }

  get presentCount(): number {
    return this.technicians.filter(t => t.isPresent).length;
  }

  get totalCount(): number {
    return this.technicians.length;
  }
}
