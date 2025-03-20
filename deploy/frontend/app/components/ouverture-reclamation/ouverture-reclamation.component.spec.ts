import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OuvertureReclamationComponent } from './ouverture-reclamation.component';

describe('OuvertureReclamationComponent', () => {
  let component: OuvertureReclamationComponent;
  let fixture: ComponentFixture<OuvertureReclamationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OuvertureReclamationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OuvertureReclamationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
