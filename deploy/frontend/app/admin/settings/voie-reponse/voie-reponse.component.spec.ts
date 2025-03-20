import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VoieReponseComponent } from './voie-reponse.component';

describe('VoieReponseComponent', () => {
  let component: VoieReponseComponent;
  let fixture: ComponentFixture<VoieReponseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VoieReponseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VoieReponseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
