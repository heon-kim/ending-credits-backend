package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.CarDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.PensionDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.RealEstateDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CarEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.RealEstateEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.CarRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.PensionRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.RealEstateRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtcDetailService {

    private final CarRepository carRepository;
    private final RealEstateRepository realEstateRepository;
    private final PensionRepository pensionRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<CarDetailDto> getConnectedCars(UUID memberId) {
        MemberEntity member = getMember(memberId);
        return carRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(CarDetailDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RealEstateDetailDto> getConnectedRealEstates(UUID memberId) {
        MemberEntity member = getMember(memberId);
        return realEstateRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(RealEstateDetailDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PensionDetailDto> getConnectedPensions(UUID memberId) {
        MemberEntity member = getMember(memberId);
        return pensionRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(PensionDetailDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarDetailDto addCar(UUID memberId, String model, String carNumber, Long purchasePrice, Long currentPrice, Integer milage, Integer manufactureYear) {
        MemberEntity member = getMember(memberId);

        AssetEntity asset = AssetEntity.builder()
                .member(member)
                .assetType(AssetType.CAR)
                .amount(purchasePrice)
                .build();

        CarEntity car = CarEntity.builder()
                .asset(asset)
                .model(model)
                .carNumber(carNumber)
                .purchasePrice(purchasePrice)
                .currentMarketPrice(currentPrice)
                .mileage(milage)
                .manufactureYear(manufactureYear)
                .isConnected(true)
                .build();

        carRepository.save(car);
        return CarDetailDto.fromEntity(car);
    }

    @Transactional
    public RealEstateDetailDto addRealEstate(UUID memberId, String name, String address, Long purchasePrice, Long currentPrice){
        MemberEntity member = getMember(memberId);

        AssetEntity asset = AssetEntity.builder()
                .member(member)
                .assetType(AssetType.REAL_ESTATE)
                .amount(currentPrice)
                .build();

        RealEstateEntity realEstate = RealEstateEntity.builder()
                .asset(asset)
                .realEstateName(name)
                .address(address)
                .purchasePrice(purchasePrice)
                .currentPrice(currentPrice)
                .isConnected(true)
                .build();

        realEstateRepository.save(realEstate);
        return RealEstateDetailDto.fromEntity(realEstate);
    }

    @Transactional
    public CarDetailDto updateCarPurchasePrice(UUID carId, Long newCurrentPurchasePrice){
        CarEntity car = carRepository.findById(carId).orElseThrow(IllegalArgumentException::new);

        CarEntity updatedCar = CarEntity.builder()
                .carId(car.getCarId())
                .asset(car.getAsset())
                .model(car.getModel())
                .carNumber(car.getCarNumber())
                .purchasePrice(car.getPurchasePrice())
                .currentMarketPrice(newCurrentPurchasePrice)
                .mileage(car.getMileage())
                .manufactureYear(car.getManufactureYear())
                .isConnected(car.isConnected())
                .build();

        carRepository.save(updatedCar);
        return CarDetailDto.fromEntity(updatedCar);
    }

    @Transactional
    public RealEstateDetailDto updateRealEstatePurchasePrice(UUID realEstateId, Long newPurchasePrice) {
        RealEstateEntity realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부동산 자산을 찾을 수 없습니다. ID: " + realEstateId));

        // 새 RealEstateEntity 인스턴스를 생성
        RealEstateEntity updatedRealEstate = RealEstateEntity.builder()
                .realEstateId(realEstate.getRealEstateId())
                .asset(realEstate.getAsset())
                .realEstateName(realEstate.getRealEstateName())
                .address(realEstate.getAddress())
                .purchasePrice(newPurchasePrice) // 새로운 구매 가격 설정
                .currentPrice(realEstate.getCurrentPrice())
                .isConnected(realEstate.isConnected())
                .build();

        realEstateRepository.save(updatedRealEstate);
        return RealEstateDetailDto.fromEntity(updatedRealEstate);
    }

    private MemberEntity getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + memberId));
    }
}
