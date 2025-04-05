import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNewTicketComponent } from './create-new-ticket.component';

describe('CreateNewTicketComponent', () => {
  let component: CreateNewTicketComponent;
  let fixture: ComponentFixture<CreateNewTicketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateNewTicketComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateNewTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
