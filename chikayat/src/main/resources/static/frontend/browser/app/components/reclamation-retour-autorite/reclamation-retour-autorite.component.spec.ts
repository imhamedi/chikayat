import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationRetourAutoriteComponent } from './reclamation-retour-autorite.component';

describe('ReclamationRetourAutoriteComponent', () => {
  let component: ReclamationRetourAutoriteComponent;
  let fixture: ComponentFixture<ReclamationRetourAutoriteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReclamationRetourAutoriteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReclamationRetourAutoriteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
