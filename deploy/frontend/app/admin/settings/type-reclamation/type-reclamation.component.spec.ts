import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeReclamationComponent } from './type-reclamation.component';

describe('TypeReclamationComponent', () => {
  let component: TypeReclamationComponent;
  let fixture: ComponentFixture<TypeReclamationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TypeReclamationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TypeReclamationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
