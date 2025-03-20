import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationEnvoiReclamantComponent } from './reclamation-envoi-reclamant.component';

describe('ReclamationEnvoiReclamantComponent', () => {
  let component: ReclamationEnvoiReclamantComponent;
  let fixture: ComponentFixture<ReclamationEnvoiReclamantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReclamationEnvoiReclamantComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReclamationEnvoiReclamantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
