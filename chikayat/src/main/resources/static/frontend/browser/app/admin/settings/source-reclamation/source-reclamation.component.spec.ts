import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SourceReclamationComponent } from './source-reclamation.component';

describe('SourceReclamationComponent', () => {
  let component: SourceReclamationComponent;
  let fixture: ComponentFixture<SourceReclamationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SourceReclamationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SourceReclamationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
