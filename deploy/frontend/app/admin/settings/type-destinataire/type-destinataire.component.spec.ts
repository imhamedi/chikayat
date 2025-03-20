import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeDestinataireComponent } from './type-destinataire.component';

describe('TypeDestinataireComponent', () => {
  let component: TypeDestinataireComponent;
  let fixture: ComponentFixture<TypeDestinataireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TypeDestinataireComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TypeDestinataireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
