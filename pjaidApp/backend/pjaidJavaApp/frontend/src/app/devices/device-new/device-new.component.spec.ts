import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceNewComponent } from './device-new.component';

describe('DeviceNewComponent', () => {
  let component: DeviceNewComponent;
  let fixture: ComponentFixture<DeviceNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeviceNewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
