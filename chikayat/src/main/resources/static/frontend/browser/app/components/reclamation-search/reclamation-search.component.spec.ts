import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationSearchComponent } from './reclamation-search.component';

describe('ReclamationSearchComponent', () => {
  let component: ReclamationSearchComponent;
  let fixture: ComponentFixture<ReclamationSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReclamationSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReclamationSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
