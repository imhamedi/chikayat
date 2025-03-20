import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerritoiresComponent } from './territoires.component';

describe('TerritoiresComponent', () => {
  let component: TerritoiresComponent;
  let fixture: ComponentFixture<TerritoiresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TerritoiresComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TerritoiresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
