import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClotureReclamationComponent } from './cloture-reclamation.component';

describe('ClotureReclamationComponent', () => {
  let component: ClotureReclamationComponent;
  let fixture: ComponentFixture<ClotureReclamationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClotureReclamationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClotureReclamationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
