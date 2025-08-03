import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationDetailsMapComponent } from './location-details-map.component';

describe('LocationDetailsMapComponent', () => {
  let component: LocationDetailsMapComponent;
  let fixture: ComponentFixture<LocationDetailsMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationDetailsMapComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LocationDetailsMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
