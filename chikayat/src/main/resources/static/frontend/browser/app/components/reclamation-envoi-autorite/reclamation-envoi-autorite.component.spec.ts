import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationEnvoiAutoriteComponent } from './reclamation-envoi-autorite.component';

describe('ReclamationEnvoiAutoriteComponent', () => {
  let component: ReclamationEnvoiAutoriteComponent;
  let fixture: ComponentFixture<ReclamationEnvoiAutoriteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReclamationEnvoiAutoriteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReclamationEnvoiAutoriteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
