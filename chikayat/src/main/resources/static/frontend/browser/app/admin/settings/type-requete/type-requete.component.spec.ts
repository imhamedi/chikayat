import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeRequeteComponent } from './type-requete.component';

describe('TypeRequeteComponent', () => {
  let component: TypeRequeteComponent;
  let fixture: ComponentFixture<TypeRequeteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TypeRequeteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TypeRequeteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
